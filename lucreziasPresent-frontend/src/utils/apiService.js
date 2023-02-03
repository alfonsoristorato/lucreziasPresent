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
    const user = await callApi("login", "POST", data);
    user.password = data.password;
    setAuthenticated(user);
    if (user.firstLogin === true) {
      navigate("/change-password");
    } else {
      navigate("/");
    }
  } catch (error) {
    setAuthError(error.response.data);
  }
};

export const changePassword = async (
  authenticated,
  data,
  setPasswordChangeRequested
) => {
  setPasswordChangeRequested(false);
  if (data.newPassword === data.newPasswordRepeated) {
    try {
      data.username = authenticated.username;
      delete data.newPasswordRepeated;
      const passwordChange = await callApi(
        "change-password",
        "POST",
        data,
        authenticated
      );
      setPasswordChangeRequested(passwordChange);
    } catch (error) {
      setPasswordChangeRequested(error.response.data);
    }
  } else {
    setPasswordChangeRequested(
      "Assicurati di aver ripetuto la nuova password."
    );
  }
};

export const editUserAttempts = async (
  userId,
  currentAttempts,
  setIsLoading,
  setUsers,
  authenticated
) => {
  setIsLoading(true);
  await callApi(
    `user/attempts/${userId}`,
    "PATCH",
    { newAttempts: currentAttempts < 4 ? 4 : 0 },
    authenticated
  );
  getUsers(setUsers, authenticated);
  setIsLoading(false);
};

export const editUserRole = async (
  userId,
  currentRole,
  setIsLoading,
  setUsers,
  authenticated
) => {
  setIsLoading(true);
  await callApi(
    `user/role/${userId}`,
    "PATCH",
    { newRole: currentRole === "admin" ? "utente" : "admin" },
    authenticated
  );
  getUsers(setUsers, authenticated);
  setIsLoading(false);
};

export const addUser = async (
  newUserName,
  setIsLoading,
  setUsers,
  authenticated,
  setNewUserMessage,
  setNewUserName
) => {
  try {
    setIsLoading(true);
    const newPassword = await callApi(
      "user",
      "POST",
      {
        username: newUserName,
      },
      authenticated
    );
    getUsers(setUsers, authenticated);

    setNewUserMessage(
      `Utente ${newUserName} aggiunto, la password temporanea è: ${newPassword}`
    );
    setNewUserName("");
    setIsLoading(false);
  } catch (error) {
    throw error;
  }
};

export const resetUserPassword = async (
  userId,
  username,
  setIsLoading,
  setUsers,
  authenticated,
  setNewUserMessage
) => {
  setIsLoading(true);
  const newPassword = await callApi(
    `user/reset-password/${userId}`,
    "PATCH",
    null,
    authenticated
  );
  getUsers(setUsers, authenticated);
  setNewUserMessage(
    `Password per ${username} cambiata, la password temporanea è: ${newPassword}`
  );
  setIsLoading(false);
};

const getUsers = async (setUsers, authenticated) => {
  try {
    const users = await callApi("user", "GET", null, authenticated);
    setUsers(users.filter((user) => user.username !== authenticated.username));
  } catch (error) {
    throw error;
  }
};
