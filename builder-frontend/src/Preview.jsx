import { createSignal, createResource, ErrorBoundary } from "solid-js";
import FormRenderer from "./FormRenderer";
import Results from "./Results";

export default function Preview({ screenerName }) {
  //   const [data] = createResource(() => fetchScreenerData(screenerName));
  const [results, setResults] = createSignal();

  const schema = {
    components: [
      {
        text: "Philly Tax Benefit",
        type: "text",
        layout: {
          row: "Row_1y05iqm",
          columns: null,
        },
        id: "Field_1wvy7sr",
      },
      {
        label: "Season",
        values: [
          {
            label: "Value",
            value: "value",
          },
          {
            label: "Summer",
            value: "summer",
          },
          {
            label: "Winter",
            value: "winter",
          },
        ],
        type: "select",
        layout: {
          row: "Row_1opwfd7",
          columns: null,
        },
        id: "Field_0yalsxi",
        key: "select_7qzimg",
      },
      {
        label: "Checkbox group",
        values: [
          {
            label: "Do you live in philly?",
            value: "value",
          },
        ],
        type: "checklist",
        layout: {
          row: "Row_0hwvoae",
          columns: null,
        },
        id: "Field_1ckvy4y",
        key: "checklist_3zsdrj",
      },
    ],
    type: "default",
    id: "Form_1sgem74",
    exporter: {
      name: "form-js (https://demo.bpmn.io)",
      version: "1.15.0",
    },
    schemaVersion: 18,
  };

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

  const submitForm = async () => {
    console.log("submit form");
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
              submitForm={submitForm}
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
