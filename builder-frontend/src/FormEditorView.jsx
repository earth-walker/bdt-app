import { onMount, onCleanup } from "solid-js";
import { FormEditor } from "@bpmn-io/form-js-editor";
import "@bpmn-io/form-js/dist/assets/form-js.css";
import "@bpmn-io/form-js-editor/dist/assets/form-js-editor.css";

function FormEditorView({ schema, submitForm }) {
  let container;

  onMount(() => {
    let formEditor = new FormEditor({ container });

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

    formEditor.importSchema(schema).catch((err) => {
      console.error("Failed to load schema", err);
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
