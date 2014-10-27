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
package be.angelcorp.celest.physics.quantities;

import be.angelcorp.celest.math.geometry.Vec3;
import be.angelcorp.celest.math.geometry.Vec3$;

public class Torque implements Cloneable {

    private Vec3 torque;

    public Torque() {
        this(Vec3$.MODULE$.zero());
    }

    public Torque(Force f, Vec3 arm) {
        setTorque(arm.cross(f.getForce()));
    }

    public Torque(Torque torque2) {
        setTorque(Vec3$.MODULE$.apply(torque2.getTorque()));
    }

    public Torque(Vec3 torque) {
        setTorque(torque);
    }

    public Torque(Vec3 force, Vec3 arm) {
        setTorque(arm.cross(force));
    }

    public void add(Torque torque2) {
        torque = torque.$plus(torque2.getTorque());
    }

    public void add(Vec3 term) {
        torque = torque.$plus(term);
    }

    @Override
    public Torque clone() {
        return new Torque(this);
    }

    public Vec3 getTorque() {
        return torque;
    }

    public void setTorque(Vec3 torque) {
        this.torque = torque;
    }

    @Override
    public String toString() {
        return String.format("[Torque t:%s]", getTorque());
    }
}
