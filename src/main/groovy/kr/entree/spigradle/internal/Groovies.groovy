/*
 * Copyright (c) 2020 Spigradle contributors.
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

package kr.entree.spigradle.internal

import org.gradle.api.plugins.ExtraPropertiesExtension

/**
 * Created by JunHyung Lim on 2020-05-16
 */
class Groovies {
    static ExtraPropertiesExtension getExtensionFrom(object) {
        return object.ext // For dynamic call
    }
}
