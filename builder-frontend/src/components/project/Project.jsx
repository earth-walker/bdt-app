import { createSignal, onMount } from "solid-js";
import {
  getTabFromStorage,
  saveTabToStorage,
} from "../../storageUtils/storageUtils";
import "../../App.css";
import Header from "../Header";
import FormEditorView from "./FormEditorView";
import Preview from "./Preview";
import Publish from "./Publish";
import KogitoDmnEditorView from "./KogitoDmnEditorView";
import ImportModels from "./ImportModels";
import { act } from "react";

function Project({ selectedProject, setSelectedProject, clearUserState }) {
  const [activeTab, setActiveTab] = createSignal("DMN Editor");

  onMount(() => {
    const tab = getTabFromStorage();
    if (tab) {
      setActiveTab(tab);
    }
  });

  const handleSelectTab = (tab) => {
    saveTabToStorage(tab);
    setActiveTab(tab);
  };

  return (
    <div className="h-screen flex flex-col">
      <Header
        returnToProjectsList={() => setSelectedProject(undefined)}
        clearUserState={clearUserState}
      ></Header>
      <div class="flex border-b border-gray-200">
        <span className="py-2 px-4 font-bold text-gray-600">
          {" "}
          {selectedProject().screenerName}
        </span>
        {[
          "DMN Editor",
          "Import Models",
          "Form Editor",
          "Preview",
          "Publish",
        ].map((tab) => (
          <button
            class={`px-4 py-2 -mb-px text-sm font-medium border-b-2 transition-colors ${
              activeTab() === tab
                ? "border-b border-gray-700 text-gray-700 hover:bg-gray-200"
                : "border-transparent text-gray-500 hover:text-gray-700 hover:bg-gray-200"
            }`}
            onClick={() => handleSelectTab(tab)}
          >
            {tab.charAt(0).toUpperCase() + tab.slice(1)}
          </button>
        ))}
      </div>

      {activeTab() == "Form Editor" && <FormEditorView></FormEditorView>}
      {activeTab() == "DMN Editor" && (
        <KogitoDmnEditorView></KogitoDmnEditorView>
      )}
      {activeTab() == "Import Models" && <ImportModels></ImportModels>}
      {activeTab() == "Preview" && <Preview></Preview>}
      {activeTab() == "Publish" && <Publish></Publish>}
    </div>
  );
}

export default Project;
