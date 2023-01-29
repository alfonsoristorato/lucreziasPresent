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

export const getData = async (
  setEntries,
  authenticated,
  setIsLoading,
  setUsers
) => {
  try {
    const response = await callApi("entry", "GET", null, authenticated);
    setEntries(response);
    if (authenticated.role === "admin") {
      getUsers(setUsers, authenticated);
    }
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
    await getData(setter, authenticated, setIsLoading);
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
    await getData(setter, authenticated, setIsLoading);
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
    await getData(setter, authenticated, setIsLoading);
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
const getUsers = async (setUsers, authenticated) => {
  try {
    const users = await callApi("user", "GET", null, authenticated);
    setUsers(users);
  } catch (error) {
    throw error;
  }
};
