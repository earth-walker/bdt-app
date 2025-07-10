import { createSignal, Show, Index, Switch, Match } from "solid-js";
import testData from "./testData.js";

export default function EligibilityResults() {
  const [testResults, setTestResults] = createSignal({});
  setTestResults(testData);
  return (
    <div class="my-2">
      <h2 class="text-gray-600 font-bold">Eligibility Results</h2>
      <Index each={Object.keys(testResults()["benefits"])}>
        {(benefitName, index) => (
          <div class="border-gray-600 border p-4 my-4">
            <Switch>
              <Match
                when={
                  testResults()["benefits"][benefitName()]["eligibility"][
                    "result"
                  ] === true
                }
              >
                <p class="mb-2 text-green-500">Eligible</p>
              </Match>
              <Match
                when={
                  testResults()["benefits"][benefitName()]["eligibility"][
                    "result"
                  ] === null
                }
              >
                <p class="mb-2 text-yellow-500">Need more information</p>
              </Match>
              <Match
                when={
                  testResults()["benefits"][benefitName()]["eligibility"][
                    "result"
                  ] === false
                }
              >
                <p class="mb-2 text-red-500">Ineligible</p>
              </Match>
            </Switch>
            <h3 class="font-bold mb-2">{benefitName()}</h3>
            <div class="mb-2">
              <Index
                each={Object.keys(
                  testResults()["benefits"][benefitName()]["eligibility"][
                    "checks"
                  ]
                )}
              >
                {(checkName, index) => (
                  <p>
                    <Switch>
                      <Match
                        when={
                          testResults()["benefits"][benefitName()][
                            "eligibility"
                          ]["checks"][checkName()] === true
                        }
                      >
                        <span class="mb-2 mr-1 text-green-500">O</span>
                      </Match>
                      <Match
                        when={
                          testResults()["benefits"][benefitName()][
                            "eligibility"
                          ]["checks"][checkName()] === null
                        }
                      >
                        <span class="mb-2 mr-1 text-yellow-500">O</span>
                      </Match>
                      <Match
                        when={
                          testResults()["benefits"][benefitName()][
                            "eligibility"
                          ]["checks"][checkName()] === false
                        }
                      >
                        <span class="mb-2 mr-1 text-red-500">O</span>
                      </Match>
                    </Switch>
                    <span>{checkName()}</span>
                  </p>
                )}
              </Index>
            </div>
            <p class="mb-2">
              {testResults()["benefits"][benefitName()]["info"]}
            </p>
            <a
              href={testResults()["benefits"][benefitName()]["appLink"]}
              target="_blank"
              class="text-sky-600 underline"
            >
              Apply Now
            </a>
          </div>
        )}
      </Index>
    </div>
  );
}
