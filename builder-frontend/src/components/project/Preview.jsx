import { createSignal, createResource, ErrorBoundary } from "solid-js";
import {
  getFormSchemaFromStorage,
  getSelectedProjectFromStorage,
} from "../../storageUtils/storageUtils";
import { submitForm } from "../../api/screener";
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

  const handleSubmitForm = async (data) => {
    let results = await submitForm(selectedProject.id, data);
    setResults(results);
  };

  return (
    <>
      <div>
        <FormRenderer
          schema={schema}
          submitForm={handleSubmitForm}
        ></FormRenderer>
        <div className="pt-4">
          <Results results={results}></Results>
        </div>
      </div>
    </>
  );
}
