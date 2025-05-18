import { onMount } from "solid-js";
import { Form } from "@bpmn-io/form-js-viewer";

import "@bpmn-io/form-js/dist/assets/form-js.css";

function FormRenderer() {
  let container;

  let schema = {
    components: [
      {
        text: "What Dish To Make?",
        type: "text",
        layout: {
          row: "Row_0i252b2",
          columns: null,
        },
        id: "Field_1ejqxvr",
      },
      {
        label: "What season is it?",
        values: [
          {
            label: "Spring",
            value: "Spring",
          },
          {
            label: "Summer",
            value: "Summer",
          },
          {
            label: "Fall",
            value: "Fall",
          },
          {
            label: "Winter",
            value: "Winter",
          },
        ],
        type: "select",
        layout: {
          row: "Row_14pnbj9",
          columns: null,
        },
        id: "Field_1violuj",
        key: "season",
      },
      {
        label: "How many guests?",
        type: "number",
        layout: {
          row: "Row_0etabra",
          columns: null,
        },
        id: "Field_03n445p",
        key: "guestCount",
      },
    ],
    type: "default",
    id: "Form_1df4wus",
    exporter: {
      name: "form-js (https://demo.bpmn.io)",
      version: "1.15.0",
    },
    schemaVersion: 18,
  };

  onMount(() => {
    const form = new Form({ container });
    form.importSchema(schema);
  });

  return <div ref={(el) => (container = el)} />;
}

export default FormRenderer;
