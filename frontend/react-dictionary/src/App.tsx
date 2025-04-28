import React from "react";
import AddWordForm from "./components/AddWordForm.tsx";
import WordList from "./components/WordList.tsx";

function App() {
  return (
    <div className="container mx-auto p-6">
      <AddWordForm />
      <WordList />
    </div>
  );
}

export default App;
