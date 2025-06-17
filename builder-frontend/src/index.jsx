/* @refresh reload */
import { render } from "solid-js/web";
import "./index.css";
import App from "./App.jsx";
import { AuthProvider } from "./context/AuthContext.jsx";

const root = document.getElementById("root");

render(
  () => (
    <AuthProvider>
      <App />
    </AuthProvider>
  ),
  root
);
