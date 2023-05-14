import { configureStore } from "@reduxjs/toolkit";
import errorModalReducer from "./utils/reduxErrorModalSlice";
export default configureStore({
  reducer: {
    errorModal: errorModalReducer,
  },
});
