/*
 * Copyright 2017 Kailuo Wang
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

package mainecoon
package tests


import cats.laws.discipline.SerializableTests
import mainecoon.laws.discipline.CartesianKTests
import mainecoon.laws.discipline.CartesianKTests.IsomorphismsK.Tuple3K

import util.{Success, Try}

class autoCartesianKTests extends MainecoonTestSuite {
  test("simple product") {

    val prodInterpreter = Interpreters.tryInterpreter.productK(Interpreters.lazyInterpreter)

    prodInterpreter.parseInt("3").first should be(Success(3))
    prodInterpreter.parseInt("3").second.value should be(3)
    prodInterpreter.parseInt("sd").first.isSuccess should be(false)
    prodInterpreter.divide(3f, 3f).second.value should be(1f)

  }

  implicit def eqT32 = SafeAlg.eqForParseAlg[Tuple3K[Try, Option, List, ?]] //have to help scalac here

  checkAll("ParseAlg[Option]", CartesianKTests[SafeAlg].cartesianK[Try, Option, List])
  checkAll("CartesianK[ParseAlg]", SerializableTests.serializable(CartesianK[SafeAlg]))
}

