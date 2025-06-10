import { createSignal } from "solid-js";
import "./App.css";
import Header from "./Header";
import FormEditorView from "./FormEditorView";
import DmnEditorView from "./DmnEditorView";
import Preview from "./Preview";
import Publish from "./Publish";

function Project({ selectedProject, setSelectedProject }) {
  const [activeTab, setActiveTab] = createSignal("Form Editor");
  const [isFormDirty, setIsFormDirty] = createSignal(false);
  const [isDmnDirty, setIsDmnDirty] = createSignal(false);
  let isFormSchemaDirty;
  let isDmnModelDirty;

  const projectName = "Test Project";
  return (
    <div className="h-screen flex flex-col">
      <Header
        returnToProjectsList={() => setSelectedProject(undefined)}
      ></Header>
      <div class="flex border-b border-gray-200">
        <span className="py-2 px-4 font-bold text-gray-600">
          {" "}
          {selectedProject().screenerName}
        </span>
        {["Form Editor", "DMN Editor", "Preview", "Publish"].map((tab) => (
          <button
            class={`px-4 py-2 -mb-px text-sm font-medium border-b-2 transition-colors ${
              activeTab() === tab
                ? "border-b border-gray-700 text-gray-700 hover:bg-gray-200"
                : "border-transparent text-gray-500 hover:text-gray-700 hover:bg-gray-200"
            }`}
            onClick={() => setActiveTab(tab)}
          >
            {tab.charAt(0).toUpperCase() + tab.slice(1)}
          </button>
        ))}
        <div className="flex ml-auto mr-8 gap-2 justify-center">
          {(isFormDirty() || isDmnDirty()) && (
            <span className="text-sm flex items-center text-gray-500">
              unsaved changes
            </span>
          )}
          <button className="m-1 px-2 text-emerald-500 border-2 rounded hover:bg-emerald-100">
            Save
          </button>
        </div>
      </div>

      {activeTab() == "Form Editor" && (
        <FormEditorView setIsFormDirty={setIsFormDirty}></FormEditorView>
      )}
      {activeTab() == "DMN Editor" && (
        <DmnEditorView setIsDmnDirty={setIsDmnDirty}></DmnEditorView>
      )}
      {activeTab() == "Preview" && <Preview></Preview>}
      {activeTab() == "Publish" && <Publish></Publish>}
    </div>
  );
}

export default Project;
