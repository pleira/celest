/**
 * Copyright (C) 2012 Simon Billemont <simon@angelcorp.be>
 *
 * Licensed under the Non-Profit Open Software License version 3.0
 * (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *        http://www.opensource.org/licenses/NOSL3.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package be.angelcorp.libs.celest.ephemeris.jplDE

import org.parboiled.scala._
import org.parboiled.errors._
import be.angelcorp.libs.celest.time.JulianDate
import be.angelcorp.libs.util.physics.Time
import org.slf4j.LoggerFactory
import be.angelcorp.libs.celest.time.timeStandard.TimeStandards

/**
 * A parser using the parboiled library to parse an ascii JPL DE ephemeris file to a [[JplDeNode]]
 */
class JplDeAsciiParser extends Parser {

    private val logger = LoggerFactory.getLogger(getClass());

    def Ephemeris = rule {
        (Size ~
            (Group101 ~ Group103 ~ Group104 ~ Group105) ~~> ((A, B, C, D) => JplDeHeader(A, B, C, D)) ~
            Group107 ~
            zeroOrMore(ANY) ~ EOI) ~~> ((S, H, D) => JplDe(S, H, D))
    }

    def Size = rule {
        (str("KSIZE") ~ optional("=") ~ WhiteSpace ~ Integer ~ WhiteSpace ~
            str("NCOEFF") ~ optional("=") ~ WhiteSpace ~ Integer ~ WhiteSpace) ~~>
            JplDeSize
    }

    def Group101 = rule {
        (str("GROUP") ~ WhiteSpace ~ str("1010") ~ WhiteSpace ~ nTimes(3, Line) ~ WhiteSpace) ~~>
            JplDeHeaderGroup101
    }
    def Group103 = rule {
        (str("GROUP") ~ WhiteSpace ~ str("1030") ~ WhiteSpace ~ nTimes(3, Float ~ WhiteSpace) ~ WhiteSpace) ~~>
            (l => JplDeHeaderGroup103(new JulianDate(l(0), TimeStandards.TDB), new JulianDate(l(1), TimeStandards.TDB), l(2)))
    }
    def Group104 = rule {
        (Group1040 ~ Group1041) ~~>
            { (size0, keys, size1, values) =>
                if (keys.size != values.size)
                    throw new ParsingException("The size of keys in group 1040 )" + keys.size +
                        ") do not match the size of values in group 1041 (" + values.size + ")")
                JplDeHeaderGroup104(keys zip values toMap)
            }
    }
    def Group1040 = rule {
        (str("GROUP") ~ WhiteSpace ~ str("1040") ~ WhiteSpace ~ Integer ~ WhiteSpace ~ oneOrMore(Keyword ~ WhiteSpace))
    }
    def Group1041 = rule {
        (str("GROUP") ~ WhiteSpace ~ str("1041") ~ WhiteSpace ~ Integer ~ WhiteSpace ~ oneOrMore(Float ~ WhiteSpace))
    }
    def Group105 = rule {
        (str("GROUP") ~ WhiteSpace ~ str("1050") ~ WhiteSpace ~ oneOrMore(Integer ~ WhiteSpace)) ~~>
            (l => {
                JplDeHeaderGroup105(l.sliding(3).toList.transpose.collect { case List(x, y, z) => (x, y, z) })
            })
    }
    def Group107 = rule {
        (str("GROUP") ~ WhiteSpace ~ str("1070") ~ WhiteSpace ~ zeroOrMore(nTimes(2, Integer ~ WhiteSpace) ~ oneOrMore(Float ~ WhiteSpace))) ~~>
            (l => l.map(meta => JplDeDataRecond(
                meta._1(0),
                meta._1(1),
                new JulianDate(meta._2(0), TimeStandards.TDB),
                new JulianDate(meta._2(1), TimeStandards.TDB),
                meta._2.slice(2, meta._2.size))))
    }

    def Line = rule { (zeroOrMore(!(anyOf("\r\n")) ~ ANY) ~ (str("\r\n") | "\r" | "\n" | EOI)) ~> (s => s) }
    def Keyword = rule { (!str("GROUP") ~ oneOrMore("0" - "9" | "a" - "z" | "A" - "Z")) ~> (s => s) }
    def Character = rule { EscapedChar | NormalChar }

    def Number = rule { (Integer | Float) }
    def Integer = rule(SuppressSubnodes) { (optional(Sign) ~ (("1" - "9") ~ Digits | Digit)) ~> (s => s toInt) }
    def Float = rule { (Exponential | Fractional ~> (s => s toDouble)) }
    def Exponential = rule(SuppressSubnodes) { group(Fractional ~ Exponent) ~> (s => s.replaceAll("[dDe]", "E") toDouble) }
    def Fractional = rule(SuppressSubnodes) { optional(Sign) ~ ((oneOrMore(Digits) ~ "." ~ optional(zeroOrMore(Digits))) | (zeroOrMore(Digits) ~ "." ~ oneOrMore(Digits))) }

    def EscapedChar = rule { "\\" ~ (anyOf("\"\\/bfnrt") | Unicode) }
    def NormalChar = rule { !anyOf("\"\\") ~ ANY }
    def Unicode = rule { "u" ~ HexDigit ~ HexDigit ~ HexDigit ~ HexDigit }

    def Digits = rule { oneOrMore(Digit) }
    def Exponent = rule { ((ignoreCase("e") | ignoreCase("d")) ~ optional(Sign) ~ Digits) }
    def Digit = rule { "0" - "9" }
    def HexDigit = rule { "0" - "9" | "a" - "f" | "A" - "Z" }
    def Sign = rule { anyOf("+-") }

    def WhiteSpace = rule(SuppressSubnodes) { zeroOrMore(anyOf(" \n\r\t\f")) }

    override implicit def toRule(string: String) = {
        if (string.endsWith(" "))
            str(string.trim) ~ WhiteSpace
        else
            str(string)
    }

    def parse(ascii: String) = {
        ReportingParseRunner(Ephemeris).run(ascii)
    }

}
