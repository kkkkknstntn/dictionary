import React, { useState } from "react";
import axios from "axios";

const AddWordForm: React.FC = () => {
  const [word, setWord] = useState("");
  const [definition, setDefinition] = useState("");
  const [audioFile, setAudioFile] = useState<File | null>(null);
  const [videoFile, setVideoFile] = useState<File | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError("");
    setSuccess(false);

    const formData = new FormData();
    formData.append("word", word);
    formData.append("definition", definition);
    if (audioFile) formData.append("audioFile", audioFile);
    if (videoFile) formData.append("videoFile", videoFile);

    try {
      const response = await axios.post("http://localhost:8080/api/words", formData, {
        headers: {
          "Content-Type": "multipart/form-data"
        },
        timeout: 60000 // 60 секунд таймаут на случай больших файлов
      });

      setSuccess(true);
      setWord("");
      setDefinition("");
      setAudioFile(null);
      setVideoFile(null);
    } catch (err: any) {
      console.error("Ошибка при загрузке:", err);
      setError("Ошибка при загрузке. Попробуйте ещё раз.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-md mx-auto p-4 border rounded-xl shadow">
      <h2 className="text-xl font-semibold mb-4">Добавить слово</h2>
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block">Слово:</label>
          <input
            type="text"
            value={word}
            onChange={(e) => setWord(e.target.value)}
            className="w-full border px-2 py-1 rounded"
            required
          />
        </div>

        <div>
          <label className="block">Определение:</label>
          <textarea
            value={definition}
            onChange={(e) => setDefinition(e.target.value)}
            className="w-full border px-2 py-1 rounded"
            required
          />
        </div>

        <div>
          <label className="block">Аудио файл (опц.):</label>
          <input
            type="file"
            accept="audio/*"
            onChange={(e) => setAudioFile(e.target.files?.[0] || null)}
          />
        </div>

        <div>
          <label className="block">Видео файл (опц.):</label>
          <input
            type="file"
            accept="video/*"
            onChange={(e) => setVideoFile(e.target.files?.[0] || null)}
          />
        </div>

        <button
          type="submit"
          className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
          disabled={loading}
        >
          {loading ? "Загрузка..." : "Отправить"}
        </button>

        {error && <p className="text-red-600">{error}</p>}
        {success && <p className="text-green-600">Слово успешно добавлено!</p>}
      </form>
    </div>
  );
};

export default AddWordForm;
