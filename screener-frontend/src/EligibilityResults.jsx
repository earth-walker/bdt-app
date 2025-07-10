import { createSignal, Show, Index } from "solid-js";
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
            <h3 class="font-bold mb-2">
              <span>{benefitName()}</span>
              <span>
                {" "}
                -{" "}
                <Show
                  when={
                    testResults()["benefits"][benefitName()]["eligibility"][
                      "result"
                    ] !== null
                  }
                  fallback={"null"}
                >
                  {testResults()["benefits"][benefitName()]["eligibility"][
                    "result"
                  ].toString()}
                </Show>
              </span>
            </h3>
            <div class="mb-2">
              <Index
                each={Object.keys(
                  testResults()["benefits"][benefitName()]["eligibility"][
                    "checks"
                  ]
                )}
              >
                {(checkName, index) => (
                  <>
                    <p>
                      {checkName()} -{" "}
                      <Show
                        when={
                          testResults()["benefits"][benefitName()][
                            "eligibility"
                          ]["checks"][checkName()] !== null
                        }
                        fallback={"null"}
                      >
                        {testResults()["benefits"][benefitName()][
                          "eligibility"
                        ]["checks"][checkName()].toString()}
                      </Show>
                    </p>
                  </>
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
