const BASE_URL = import.meta.env.VITE_API_URL;

export const fetchScreenerData = async (screenerName) => {
  try {
    const response = await fetch(`${BASE_URL}screener/${screenerName}`);
    if (!response.ok) {
      throw new Error(`Error: ${response.status}`);
    }
    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Error: failed to fetch screener form data", error);
    throw error;
  }
};

export const getDecisionResult = async (screenerName, data) => {
  try {
    data.screenerName = screenerName;
    const response = await fetch(`${BASE_URL}decision`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    });

    if (!response.ok) {
      throw new Error("Failed to submit form");
    }

    const result = await response.json();
    return result;
  } catch (err) {
    console.log("Error submitting form");
    console.log(err);
  }
};
