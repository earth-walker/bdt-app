import "./App.css";
import Home from "./Home";
import Screener from "./Screener";

function App() {
  const urlParams = new URLSearchParams(window.location.search);
  const screener = urlParams.get("screener");
  if (!screener)
    return (
      <>
        <Home></Home>
      </>
    );
  else return <Screener screenerName={screener}></Screener>;
}

export default App;
