/**
 * Copyright (C) 2009-2012 simon <simon@angelcorp.be>
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
package be.angelcorp.libs.celest.frames;

import be.angelcorp.libs.util.exceptions.GenericException;

/**
 * Exception when the transformation from one frame to another failed.
 * 
 * @author Simon Billemont
 */
public class ReferenceFrameTransformationException extends GenericException {

	/**
	 * @param format
	 *            Formated string, see {@link String#format(String, Object...)}
	 * @param args
	 *            Arguments to insert in the formated string.
	 * @see String#format(String, Object...)
	 */
	public ReferenceFrameTransformationException(String format, Object... args) {
		super(format, args);
	}

	/**
	 * @param format
	 *            Formated string, see {@link String#format(String, Object...)}
	 * @param args
	 *            Arguments to insert in the formated string.
	 * @param e
	 *            Cause of the transformation exception.
	 * @see String#format(String, Object...)
	 */
	public ReferenceFrameTransformationException(Throwable e, String format, Object... args) {
		super(e, format, args);
	}

}
