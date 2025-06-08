const apiUrl = import.meta.env.VITE_API_URL;

export const fetchProjects = async () => {
  const url = apiUrl + "/screeners";
  try {
    const response = await fetch(url, {
      method: "GET",
      headers: {
        Accept: "application/json",
      },
    });

    if (!response.ok) {
      throw new Error(`Fetch failed with status: ${response.status}`);
    }
    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Error fetching projects:", error);
    throw error; // rethrow so you can handle it in your component if needed
  }
};
