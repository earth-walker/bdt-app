import { createSignal, createResource, ErrorBoundary } from "solid-js";
import FormRenderer from "./FormRenderer";
import Results from "./Results";
import { fetchScreenerData, getDecisionResult } from "./api/api";
import Loading from "./Loading";
import ErrorPage from "./Error";

export default function Screener({ screenerName }) {
  const [data] = createResource(() => fetchScreenerData(screenerName));
  const [results, setResults] = createSignal();

  const submitForm = async (data) => {
    try {
      let results = await getDecisionResult(screenerName, data);
      if (!Array.isArray(results)) {
        results = [results];
      }
      setResults(results);
    } catch (err) {
      console.log(err);
    }
  };

  return (
    <>
      <div>
        <ErrorBoundary
          fallback={(error, reset) => <ErrorPage error={error}></ErrorPage>}
        >
          {data.loading && <Loading></Loading>}
          {data() && (
            <>
              <FormRenderer
                schema={data().formModel}
                submitForm={submitForm}
              ></FormRenderer>
              <div className="pt-4">
                <Results results={results}></Results>
              </div>
            </>
          )}
        </ErrorBoundary>
      </div>
    </>
  );
}
