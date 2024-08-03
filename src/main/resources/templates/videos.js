import React from 'react';

//iframe API를 사용하여 특정비디오만 로드해오기

const VideoPlayer = ({ videoId }) => {
return (
<iframe
        width="720"
        height="405"
        src={`https://www.youtube.com/embed/${videoId}`}
        frameBorder="0"
        allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
        allowFullScreen
        title="YouTube Video Player"
></iframe>
);
};

const VideoList = ({ videoIds }) => {
return (
<div>
    {videoIds.map((videoId, index) => (
    <div key={index}>
        <h3>Video {index + 1}</h3>
        <VideoPlayer videoId={videoId} />
    </div>
    ))}
</div>
);
};

const App = () => {
const videoIds = [
'ZJjBDpOn11c',
'MTU4iCDntjs',
'vEsu_wOb9Xg',
'LIRRMdkk3IQ'
];

return (
<div>
    <h1>YouTube Video List</h1>
    <VideoList videoIds={videoIds} />
</div>
);
};

export default App;



