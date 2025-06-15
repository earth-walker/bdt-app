import { createSignal, createResource, ErrorBoundary } from "solid-js";
import {
  getFormSchemaFromStorage,
  getSelectedProjectFromStorage,
} from "../storageUtils/storageUtils";
import { submitForm } from "../api/api";
import FormRenderer from "./FormRenderer";
import Results from "./Results";

export default function Preview({ screenerName }) {
  //   const [data] = createResource(() => fetchScreenerData(screenerName));
  const [results, setResults] = createSignal();
  let selectedProject = getSelectedProjectFromStorage();
  let schema = getFormSchemaFromStorage();
  if (!schema) {
    schema = {
      components: [],
      exporter: { name: "form-js (https://demo.bpmn.io)", version: "1.15.0" },
      id: "Form_1sgem74",
      schemaVersion: 18,
      type: "default",
    };
  }

  // const submitForm = async (data) => {
  //   try {
  //     let results = await getDecisionResult(screenerName, data);
  //     if (!Array.isArray(results)) {
  //       results = [results];
  //     }
  //     setResults(results);
  //   } catch (err) {
  //     console.log(err);
  //   }
  // };

  const handleSubmitForm = async (data) => {
    let results = await submitForm(selectedProject.id, data);
    console.log("submit form");
    setResults(results);
  };

  return (
    <>
      <div>
        {/* <ErrorBoundary
          fallback={(error, reset) => <ErrorPage error={error}></ErrorPage>}
        > */}
        {/* {data.loading && <Loading></Loading>} */}
        {
          <>
            <FormRenderer
              schema={schema}
              submitForm={handleSubmitForm}
            ></FormRenderer>
            <div className="pt-4">
              <Results results={results}></Results>
            </div>
          </>
        }
        {/* </ErrorBoundary> */}
      </div>
    </>
  );
}
