import { For, Index, createSignal, Show } from "solid-js";
import LoopCard from "./LoopCard";

export default function Results({ results }) {
  const [testResults, setTestResults] = createSignal({});
  setTestResults({
    benefits: {
      phlLoop: {
        eligibility: {
          result: false,
          checks: {
            tenOrMoreYearsOwnerOccupant: null,
            notEnrolledInPhlLoop: null,
            noTenYearTaxAbatement: null,
            loopTaxAssessmentEligible: null,
            phillyOwnerOccupantHomeowner: false,
            underPreviousAnnualGrossIncome: null,
            notTaxDelinquentOrIsEnrolledInOopaEligible: null,
          },
        },
      },
      phlSeniorCitizenTaxFreeze: {
        eligibility: {
          result: false,
          checks: {
            notEnrolledInPhlSeniorCitizenTaxFreeze: null,
            meetsPhlSeniorCitizenTaxFreezeAgeRequirements: null,
            phillyOwnerOccupantHomeowner: false,
            underMaritalStatusBasedCurrentMonthlyGrossIncome: null,
          },
        },
      },
      phlLowIncomeTaxFreeze: {
        eligibility: {
          result: false,
          checks: {
            phillyOwnerOccupantHomeowner: false,
            notEnrolledInPhlLowIncomeTaxFreeze: null,
            underMaritalStatusBasedCurrentMonthlyGrossIncome: null,
          },
        },
      },
      phlOopa: {
        eligibility: {
          result: false,
          checks: {
            taxDelinquent: null,
            phillyOwnerOccupantHomeowner: false,
            notEnrolledInPhlOopa: null,
          },
        },
      },
      phlHomesteadExemption: {
        eligibility: {
          result: false,
          checks: {
            noTenYearTaxAbatement: null,
            phillyOwnerOccupantHomeowner: false,
            notEnrolledInPhlHomesteadExemption: null,
          },
        },
      },
    },
  });
  return (
    <>
      <div class="my-4 mx-8 p-4 border border-gray-300 rounded shadow-sm">
        <h2 class="text-gray-800 text-md font-bold">Results</h2>
        <div class="my-2">
          {testResults() && (
            <Index each={Object.keys(testResults()["benefits"])}>
              {(benefitName, index) => (
                <>
                  <h3 class="font-bold mt-4">
                    <span>{benefitName()}</span>
                    <span>
                      {" "}
                      -{" "}
                      <Show
                        when={
                          testResults()["benefits"][benefitName()][
                            "eligibility"
                          ]["result"] !== null
                        }
                        fallback={"null"}
                      >
                        {testResults()["benefits"][benefitName()][
                          "eligibility"
                        ]["result"].toString()}
                      </Show>
                    </span>
                  </h3>
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
                </>
              )}
            </Index>
          )}
        </div>
      </div>
    </>
  );
}
