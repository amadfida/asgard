/*
 * Copyright 2012 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netflix.asgard.model

import com.amazonaws.services.autoscaling.model.AutoScalingGroup
import com.amazonaws.services.autoscaling.model.SuspendedProcess
import com.netflix.asgard.TagNames
import com.netflix.asgard.Time
import org.joda.time.DateTime

@SuppressWarnings("GroovyAssignabilityCheck")
@Category(AutoScalingGroup)
class AutoScalingGroupMixin {

    SuspendedProcess getSuspendedProcess(AutoScalingProcessType autoScalingProcessType) {
        suspendedProcesses.find { it.processName == autoScalingProcessType.name() }
    }

    Boolean isZoneRebalancingSuspended() {
        isProcessSuspended(AutoScalingProcessType.AZRebalance)
    }

    Boolean isProcessSuspended(AutoScalingProcessType autoScalingProcessType) {
        getSuspendedProcess(autoScalingProcessType) != null
    }

    Collection<AutoScalingProcessType> getSuspendedProcessTypes() {
        AutoScalingProcessType.values().findAll {
            this.isProcessSuspended(it)
        }
    }

    DateTime getExpirationTime() {
        Time.parse(tags.find { it.name == TagNames.EXPIRATION_TIME }?.value)
    }
}
