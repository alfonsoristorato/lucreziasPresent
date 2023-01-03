import axios from "axios";

const apiOrigin = "http://localhost:8080";

export const callApi = async (route, methodUsed, bodyUsed, authenticated) => {
  try {
    const axiosInit = {
      ...(authenticated && {
        headers: {
          Authorization:
            "Basic " +
            window.btoa(authenticated.username + ":" + authenticated.password),
        },
      }),

      ...(bodyUsed && { data: bodyUsed }),
      ...(methodUsed && { method: methodUsed }),
    };

    const response = await axios.request(`${apiOrigin}/${route}`, axiosInit);
    return response.data;
  } catch (error) {
    throw error;
  }
};

export const readEntries = async (setter, authenticated) => {
  try {
    const response = await callApi("entry", "GET", null, authenticated);
    setter(response);
  } catch (error) {
    throw error;
  }
};

export const addEntry = async (setter, data, authenticated, handleClose) => {
  try {
    await callApi("entry", "POST", data, authenticated);
    await readEntries(setter, authenticated);
    handleClose();
  } catch (error) {
    throw error;
  }
};

export const login = async (setAuthenticated, setAuthError, data, navigate) => {
  try {
    await callApi("login", "POST", data);
    setAuthenticated(data);
    navigate("/");
  } catch (error) {
    setAuthError(error.response.data);
  }
};

export const editEntry = async (
  setter,
  data,
  id,
  authenticated,
  handleClose
) => {
  try {
    await callApi(`entry/${id}`, "PATCH", data, authenticated);
    await readEntries(setter, authenticated);
    handleClose();
  } catch (error) {
    throw error;
  }
};

export const deleteEntry = async (setter, id, authenticated, handleClose) => {
  try {
    await callApi(`entry/${id}`, "DELETE", null, authenticated);
    await readEntries(setter, authenticated);
    handleClose();
  } catch (error) {
    throw error;
  }
};
