$(document).ready(function () {
    document.getElementById("playlist-prev").addEventListener("click", playlistPrev);
    document.getElementById("playlist-pause").addEventListener("click", playlistPause);
    document.getElementById("playlist-play").addEventListener("click", playlistPlay);
    document.getElementById("playlist-next").addEventListener("click", playlistNext);
    document.getElementById("volume-mute").addEventListener("click", volumeMute);
    document.getElementById("playlist-heartEmpty").addEventListener("click", playlistHeartEmpty);
    document.getElementById("playlist-heart").addEventListener("click", playlistHeart);
    document.getElementById("playlist-personAdd").addEventListener("click", playlistPersonAdd);
    document.getElementById("playlist-songList").addEventListener("click", songList);


    let audio = document.getElementById("audio");
    let volumeMuteOff = document.getElementById("volume-mute-off");
    let volumeMuteOn = document.getElementById("volume-mute-on");
    let volumeProgress = document.getElementById("volume-progress");
    let timeProgress = document.getElementById("time-progress");
    let songAddInfo = document.getElementById("song-addInfo");
    let playlistClose = document.getElementById("playlist-close");


    let duration = 0;
    let volume = 0.75;

    let audioCurrent;


    // 오디오 길이로 분, 초 단위로 변환
    function getLength(value) {
        let m = ~~(value / 60), s = ~~(value % 60);

        return (m < 10 ? "0" + m : m) + ':' + (s < 10 ? "0" + s : s);
    }

    // audio의 메타데이터가 로드 됬을때 실행
    audio.onloadedmetadata = function () {
        duration = audio.duration;

        timeProgress.max = duration;

        $('.playListBar-time-duration').text(getLength(duration));
    }


    // 사운드 시간 바뀔때 current 값 수정
    timeProgress.addEventListener('change', function () {
        audioCurrent = timeProgress.value;
        audio.currentTime = audioCurrent;

        $('.playListBar-time-current').text(getLength(audioCurrent));
    })

    // 사운드 Range bar 값을 audio volume에 넣기
    volumeProgress.addEventListener('change', function () {
        volume = volumeProgress.value / 100;
        audio.volume = volume;
    })

    // 오디오 시간 업데이트
    audio.addEventListener('timeupdate', function () {

        timeProgress.value = audio.currentTime;
        $('.playListBar-time-current').text(getLength(audio.currentTime));


    })

    // 오디오 재생이 완료됬을때
    audio.addEventListener('ended', function () {

        timeProgress.value = audio.currentTime;
        $('.playListBar-time-current').text(getLength(audio.currentTime));

        // 여기에 다음걸 실행할지 멈출지 체크
        playlistPause();

        // 끝났을때 timeProgress 와 시간을 0으로
        audio.currentTime = 0;
        timeProgress.value = 0;
    })

    playlistClose.addEventListener('click', function () {
        songList();
    })


    songAddInfo.addEventListener('click', function () {
        if ($("#songInfo-dropbox").css("display") === "none") {
            document.getElementById("songInfo-dropbox").style.display = "flex";
        } else {
            document.getElementById("songInfo-dropbox").style.display = "none";
        }
    })




    function playlistPrev() {
        alert("이전 노래 재생 버튼");
    }


    function playlistPause() {
        audio.pause();
        document.getElementById("playlist-pause").style.display = "none";
        document.getElementById("playlist-play").style.display = "inline-block";
    }

    function playlistPlay() {
        audio.play();
        document.getElementById("playlist-play").style.display = "none";
        document.getElementById("playlist-pause").style.display = "inline-block";

    }

    function playlistNext() {
        alert("다음 노래 재생 버튼");
    }

    function volumeMute() {

        if (audio.muted === false) {
            audio.muted = true;
            volumeMuteOn.style.display = 'inline-block';
            volumeMuteOff.style.display = 'none';
            volumeProgress.value = 0;
            audio.volume = 0;
        } else if (audio.muted === true) {
            audio.muted = false;
            volumeMuteOn.style.display = 'none';
            volumeMuteOff.style.display = 'inline-block';
            volumeProgress.value = 100;
            audio.volume = 1;
        }


    }

    function playlistHeartEmpty() {
        document.getElementById("playlist-heartEmpty").style.display = "none";
        document.getElementById("playlist-heart").style.display = "inline-block";
    }

    function playlistHeart() {
        document.getElementById("playlist-heartEmpty").style.display = "inline-block";
        document.getElementById("playlist-heart").style.display = "none";
    }

    function playlistPersonAdd() {
        alert("노래 올린 사람 구독");
    }

    function songList() {

        if ($(".playlist-dropUp-content").css("display") === "none") {
            document.getElementById("playlist-dropUp-content").style.display = "block";
        } else {
            document.getElementById("playlist-dropUp-content").style.display = "none";
            if ($("#songInfo-dropbox").css("display") !== "none") {
                document.getElementById("songInfo-dropbox").style.display = "none";
            }
        }


    }
});
