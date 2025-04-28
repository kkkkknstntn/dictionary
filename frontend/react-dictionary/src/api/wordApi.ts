import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080",
});

export const addWord = (data: FormData) => api.post("/api/words", data);
export const getWords = () => api.get("/api/words");
