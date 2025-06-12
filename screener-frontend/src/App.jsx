import { Router, Route, Routes } from "@solidjs/router";
import "./App.css";
import Home from "./Home";
import Screener from "./Screener";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" component={Home} />
        <Route path="/screener/:screenerId" component={Screener} />
      </Routes>
    </Router>
  );
}

export default App;
