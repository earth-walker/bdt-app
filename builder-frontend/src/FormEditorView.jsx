import { onMount, onCleanup } from "solid-js";
import { FormEditor } from "@bpmn-io/form-js-editor";
import {
  saveFormSchemaToStorageDebounced,
  getFormSchemaFromStorage,
} from "./storageUtils/storageUtils";
import "@bpmn-io/form-js/dist/assets/form-js.css";
import "@bpmn-io/form-js-editor/dist/assets/form-js-editor.css";

function FormEditorView({ setIsFormDirty }) {
  let container;
  let emptySchema = {
    components: [],
    exporter: { name: "form-js (https://demo.bpmn.io)", version: "1.15.0" },
    id: "Form_1sgem74",
    schemaVersion: 18,
    type: "default",
  };
  onMount(() => {
    let formEditor = new FormEditor({ container });

    let formSchema = getFormSchemaFromStorage();

    if (formSchema) {
      formEditor.importSchema(formSchema).catch((err) => {
        console.error("Failed to load schema", err);
      });
    } else {
      formEditor.importSchema(emptySchema).catch((err) => {
        console.error("Failed to load schema", err);
      });
    }

    formEditor.on("changed", (e) => {
      console.log(e);
      console.log("Form dirty");
      setIsFormDirty(true);
      saveFormSchemaToStorageDebounced(e.schema);
    });

    onCleanup(() => {
      if (formEditor) {
        formEditor.destroy();
        formEditor = null;
      }
    });
  });

  return (
    <div className="overflow-auto">
      <div className="h-full" ref={(el) => (container = el)} />
    </div>
  );
}

export default FormEditorView;
