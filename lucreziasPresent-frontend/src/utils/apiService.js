import axios from "axios";

const apiOrigin = `${process.env.REACT_APP_API}`;

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

export const readEntries = async (setter, authenticated, setIsLoading) => {
  try {
    const response = await callApi("entry", "GET", null, authenticated);
    setter(response);
    setIsLoading(false);
  } catch (error) {
    throw error;
  }
};

export const addEntry = async (
  setter,
  data,
  authenticated,
  handleClose,
  setIsLoading
) => {
  try {
    await callApi("entry", "POST", data, authenticated);
    await readEntries(setter, authenticated, setIsLoading);
    handleClose();
  } catch (error) {
    throw error;
  }
};

export const editEntry = async (
  setter,
  data,
  id,
  authenticated,
  handleClose,
  setIsLoading
) => {
  try {
    await callApi(`entry/${id}`, "PATCH", data, authenticated);
    await readEntries(setter, authenticated, setIsLoading);
    handleClose();
  } catch (error) {
    throw error;
  }
};

export const deleteEntry = async (
  setter,
  id,
  authenticated,
  handleClose,
  setIsLoading
) => {
  try {
    await callApi(`entry/${id}`, "DELETE", null, authenticated);
    await readEntries(setter, authenticated, setIsLoading);
    handleClose();
  } catch (error) {
    throw error;
  }
};

export const login = async (setAuthenticated, setAuthError, data, navigate) => {
  try {
    const role = await callApi("login", "POST", data);
    data.role = role;
    setAuthenticated(data);
    navigate("/");
  } catch (error) {
    setAuthError(error.response.data);
  }
};
export const getUsers = async (setUsers, setIsLoading, authenticated) => {
  try {
    const users = await callApi("user", "GET", null, authenticated);
    setUsers(users);
    setIsLoading(false);
  } catch (error) {
    throw error;
  }
};
