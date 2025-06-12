import { onMount, onCleanup, createSignal } from "solid-js";
import { debounce } from "lodash";
import {
  getDmnModelFromStorage,
  saveDmnModelToStorageDebounced,
  getSelectedProjectFromStorage,
} from "./storageUtils/storageUtils";
import { saveDmnModel } from "./api/api";
import DmnModeler from "dmn-js/lib/Modeler";
import "dmn-js/dist/assets/diagram-js.css";
import "dmn-js/dist/assets/dmn-font/css/dmn.css";
import "dmn-js/dist/assets/dmn-js-boxed-expression-controls.css";
import "dmn-js/dist/assets/dmn-js-boxed-expression.css";
import "dmn-js/dist/assets/dmn-js-decision-table-controls.css";
import "dmn-js/dist/assets/dmn-js-decision-table.css";
import "dmn-js/dist/assets/dmn-js-drd.css";
import "dmn-js/dist/assets/dmn-js-literal-expression.css";
import "dmn-js/dist/assets/dmn-js-shared.css";

function DmnEditorView() {
  const [isUnsaved, setIsUnsaved] = createSignal(false);
  const [isSaving, setIsSaving] = createSignal(false);
  let container;
  let timeoutId;
  let modeler;
  onMount(() => {
    initializeDmnModeler();
  });

  const initializeDmnModeler = async () => {
    modeler = new DmnModeler({ container });

    const defaultXML = `<?xml version="1.0" encoding="UTF-8"?>
      <definitions xmlns="https://www.omg.org/spec/DMN/20191111/MODEL/"
                  xmlns:dmndi="https://www.omg.org/spec/DMN/20191111/DMNDI/"
                  xmlns:di="http://www.omg.org/spec/DMN/20180521/DI/"
                  xmlns:dc="http://www.omg.org/spec/DMN/20180521/DC/"
                  id="definitions"
                  name="DRD"
                  namespace="http://camunda.org/schema/1.0/dmn">
      </definitions>`;

    // Load a sample or empty diagram
    const initialDmn = getDmnModelFromStorage();
    console.log({ initialDmn });
    if (initialDmn) {
      await modeler.importXML(initialDmn).catch((err) => {
        console.error("Failed to import DMN XML", err);
      });
    } else {
      await modeler.importXML(defaultXML).catch((err) => {
        console.error("Failed to import DMN XML", err);
      });
      // modeler.on("commandStack.changed", async () => {
      //   console.log("Change detected");
      //   const { xml } = await modeler.saveXML({ format: true });
      //   setIsDmnDirty(true);
      //   console.log(xml);
      //   debounce(() => {
      //     saveDmnModelToStorageDebounced(xml);
      //   }, 500);
      // });
      const activeEditor = modeler.getActiveViewer();
      const eventBus = activeEditor.get("eventBus");

      const debouncedSave = debounce(async () => {
        try {
          const { xml } = await modeler.saveXML({ format: true });
          console.log("Debounced XML:", xml);
        } catch (err) {
          console.error("Could not save XML", err);
        }
      }, 500);

      eventBus.on("commandStack.changed", async function (event) {
        debouncedSave();
      });

      //Try figure out how to listen in on all events not just higher DRD events

      // eventBus.on("directEditing.complete", async function (event) {
      //   debouncedSave();
      // });

      // eventBus.on("decisionTable.updated", async function (event) {
      //   debouncedSave();
      // });
    }
    onCleanup(() => {
      modeler.destroy();
      clearTimeout(timeoutId);
    });
  };

  const handleSave = async () => {
    const selectedProject = getSelectedProjectFromStorage();
    const screenerId = selectedProject.id;
    const { xml } = await modeler.saveXML({ format: true });
    setIsUnsaved(false);
    setIsSaving(true);
    saveDmnModel(screenerId, xml);
    clearTimeout(timeoutId);
    timeoutId = setTimeout(() => setIsSaving(false), 500);
  };

  return (
    <>
      <div className="h-full overflow-auto">
        <div className="h-full" ref={(el) => (container = el)} />
      </div>
      <div className="fixed z-50 top-16 right-4 flex ml-auto mr-8 gap-2 justify-center">
        {isUnsaved() && (
          <span className="underline text-sm flex items-center text-gray-500">
            unsaved changes
          </span>
        )}
        {isSaving() && (
          <span className="text-sm flex items-center text-gray-500">
            saving ...
          </span>
        )}
        <button
          onClick={handleSave}
          className="px-2 text-emerald-500 h-8 border-2 rounded hover:bg-emerald-100"
        >
          Save
        </button>
      </div>
    </>
  );
}

export default DmnEditorView;
