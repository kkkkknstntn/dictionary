import React, { useEffect, useState } from "react";
import { getWords } from "../api/wordApi";

type Word = {
  id: number;
  word: string;
  definition: string;
  audioPath?: string;
  videoPath?: string;
};

const WordList: React.FC = () => {
  const [words, setWords] = useState<Word[]>([]);

  useEffect(() => {
    getWords().then((res) => setWords(res.data));
  }, []);

  return (
    <div className="mt-8 max-w-2xl mx-auto">
      <h2 className="text-xl font-semibold mb-4">Список слов</h2>
      <ul className="space-y-4">
        {words.map((w) => (
          <li key={w.id} className="border p-4 rounded-lg shadow bg-white">
            <div className="text-lg font-bold">{w.word}</div>
            <div className="text-gray-700 mb-2">{w.definition}</div>

            {w.audioPath && (
              <div className="mt-2">
                <p className="text-sm font-medium">🔊 Аудио:</p>
                <audio controls className="w-full mt-1">
                  <source src={w.audioPath} type="audio/mpeg" />
                  Ваш браузер не поддерживает аудио.
                </audio>
              </div>
            )}

            {w.videoPath && (
              <div className="mt-4">
                <p className="text-sm font-medium">🎥 Видео:</p>
                <video controls className="w-full mt-1 max-h-80">
                  <source src={w.videoPath} type="video/mp4" />
                  Ваш браузер не поддерживает видео.
                </video>
              </div>
            )}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default WordList;
