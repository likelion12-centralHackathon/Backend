import React, { useState, useEffect } from 'react';
import axios from 'axios';

// 특정 검색어에 대한 12개의 검색 목록을 가져오는 API
const VideoPlayer = ({ title, url }) => {
    return (
        <div>
            <h3>{title}</h3>
            <iframe
                width="560"
                height="315"
                src={url.replace("https://www.youtube.com/watch?v=", "https://www.youtube.com/embed/")}
                frameBorder="0"
                allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                allowFullScreen
                title={title}
            ></iframe>
            <hr />
        </div>
    );
};

const App = () => {
    const [videos, setVideos] = useState([]);
    const searchQuery = "개발자 스트레칭"; // 예시 검색어

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.post('http://localhost:8080/api/v1/Youtube/keywordSearchData.do', null, {
                    params: { search: searchQuery }
                });
                const videoData = response.data.split('\n\n').map(video => {
                    if (video.trim()) {
                        const lines = video.split('\n');
                        const title = lines[0].replace("Title: ", "");
                        const url = lines[1].replace("URL: ", "");
                        return { title, url };
                    }
                    return null;
                }).filter(video => video !== null);
                setVideos(videoData);
            } catch (error) {
                console.error("Error:", error);
            }
        };

        fetchData();
    }, [searchQuery]);

    return (
        <div>
            <h1>YouTube Video Search</h1>
            <div id="results">
                {videos.map((video, index) => (
                    <VideoPlayer key={index} title={video.title} url={video.url} />
                ))}
            </div>
        </div>
    );
};

export default App;
