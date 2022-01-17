$(document).ready(function () {

    const audioTime = sessionStorage.getItem("audioTime");
    const audioSrc = sessionStorage.getItem("audioSrc");

    console.log("audioTime : " + audioTime);
    console.log("audioSrc : " + audioSrc);

    if(audioTime != null && audioSrc != null) {

        audio.currentSrc = audioSrc;
        audio.currentTime = audioTime;
        playlistPlay();
    }

});