import "./App.css";
import { createSignal } from "solid-js";
import Project from "./Project";
import ProjectsList from "./ProjectsList";

function App() {
  const [selectedProject, setSelectedProject] = createSignal();
  const projectName = "Test Project";

  return (
    <>
      {selectedProject() && <Project></Project>}
      {!selectedProject() && (
        <ProjectsList setSelectedProject={setSelectedProject}></ProjectsList>
      )}
    </>
  );
}

export default App;
