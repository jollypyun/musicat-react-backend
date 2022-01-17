let audio = document.getElementById("audio");

// 오디오 길이로 분, 초 단위로 변환
function getLength(value) {
    let m = ~~(value / 60), s = ~~(value % 60);

    return (m < 10 ? "0" + m : m) + ':' + (s < 10 ? "0" + s : s);
}


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


    let volumeMuteOff = document.getElementById("volume-mute-off");
    let volumeMuteOn = document.getElementById("volume-mute-on");
    let volumeProgress = document.getElementById("volume-progress");
    let timeProgress = document.getElementById("time-progress");
    let songAddInfo = document.getElementsByClassName(".song-addInfo");
    let playlistClose = document.getElementById("playlist-close");


    let duration = 0;
    let volume = 0.75;

    let audioCurrent;



    // audio의 메타데이터가 로드 됬을때 실행
    audio.onloadedmetadata = function () {
        duration = audio.duration;

        timeProgress.max = duration;

        $('.playListBar-time-duration').text(getLength(duration));

    }

    // audio.oncanplaythrough = function() {
    //
    //     if(audio.currentTime < 10)
    //         audio.currentTime=10
    //
    //     console.log("oncanplaythrough");
    //     audio.play();
    // }

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
        if ($(".songInfo-dropbox").css("display") === "none") {
            document.getElementsByClassName("songInfo-dropbox").style.display = "flex";
        } else {
            document.getElementsByClassName("songInfo-dropbox").style.display = "none";
        }
    })


    function playlistPrev() {
        alert("이전 노래 재생 버튼");
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
            if ($(".songInfo-dropbox").css("display") !== "none") {
                document.getElementsByClassName("songInfo-dropbox").style.display = "none";
            }
        }


    }
});

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

function onAddtoPlayClick(btn) {
    //var playlistNo = [[${#authentication.principal.getNo}]];

    var playlistNo = $("#userNoForPlaylist").text();
    playlistNo = playlistNo + "pl1";
    var musicNos = $(btn).children("input").val();
    console.log("playlistNo : ", playlistNo);
    console.log("musicNos : ", musicNos);
    requestProcessAddToPlay("/pushmusic/" + playlistNo, musicNos);
}

const getAjaxAddToPlay = function (url, musicNos) {
    // var objParams = {
    //     "playlistNo": playlistNo,
    //     "musicNos" : musicNos
    // };
    //console.log(objParams);
    return new Promise((resolve, reject) => {
        $.ajax({
            url: url,
            method: "POST",
            dataType: "json",
            data: {
                musicNos: musicNos
            },
            success: function (data) {
                // 비동기 작업 성공 시 호출
                resolve(data);
            },
            error: function (e) {
                // 비동기 작업 실패 시 호출
                reject(e);
            },
        });
    });
};

async function requestProcessAddToPlay(url, musicNos) {
    try {
        // await 다음에는 비동기 처리 작업이 와야함.
        //const result = await getAjaxAddToPlay(url, playlistNo, musicNos);
        const result = await getAjaxAddToPlay(url, musicNos);
        console.log(result);
        //localStorage.setItem("musics", JSON.stringify(result));
        //console.log(JSON.parse(localStorage.getItem("musics")));

        $("#currentPlayList_ul").empty(); // 만약 spa 방식으로 한다면 마지막에 추가한 1개만 리턴해서 갱신하는게 더 좋을 듯
        result.forEach(function(e){
            $("#currentPlayList_ul").append('<li><div class=\"playlist-dropUp-content-inner\"><div class=\"dropUp-inner-info\"><img src=\"' + e.links[1].href
                + '"/> <label class="playlist-dropUp-content-playBtn position-absolute"> <input type="button" id = \"' + e.links[0].href + '\" onclick = \"playAudio(this)\"><span class="material-icons position-absolute">play_circle</span></label><div class="dropUp-inner-info-text"><span>'+ e.title +'</span></div></div><div class="dropUp-inner-time"><button id="song-addInfo"><span class="material-icons">dehaze</span></button><div id="songInfo-dropbox"><button >삭제</button><button >플레이리스트 추가</button></div></div></div><div></div></li>');
             // $("#currentPlayList_ul").append('<li><div class=\"playlist-dropUp-content-inner\"><div class=\"dropUp-inner-info\"><img src=\"' + e.links[1].href
             //     + '"/> <button id = \"' + e.links[0].href + '\" onclick = \"playAudio(this)\"><span class="material-icons">play_circle</span></button><div class="dropUp-inner-info-text"><span>'+ e.title +'</span></div></div><div class="dropUp-inner-time"><span>30:30</span><button class="song-addInfo"><span class="material-icons">dehaze</span></button><div class="songInfo-dropbox"><button >삭제</button><button >플레이리스트 추가</button></div></div></div><div></div></li>');
             // $("#audioTime").text(getLength(audioCurrent));
        });
        let lastIndex = result.length-1;
        $("#audio").attr("src", result[lastIndex].links[0].href);
        $("#audio").trigger("play");
        $(".playListBar-title-song").text(result[lastIndex].title)
        $(".playListBar-inner-img").children("img").attr("src", result[lastIndex].links[1].href);

    } catch (error) {
        console.log("error : ", error);
    }
}
function playAudio(btn) {
    //console.log($(btn).attr("id"));

    $("#audio").attr("src", $(btn).attr("id"));
    //console.log($(btn).parent().parent().children(".dropUp-inner-info-text").text());
    $(".playListBar-title-song").text($(btn).parent().parent().children(".dropUp-inner-info-text").text());
    $(".playListBar-inner-img").children("img").attr("src", $(btn).parent().parent().children("img").attr("src"));
    //console.log($(btn).parent().parent().children("img"));

    $("#audio").trigger("play");

    playlistPlay();

    // $("#audio").currentTime=10;
    // console.log($("#audio").currentTime);
}

const getCurrentPlayAjax = function (url, musicNos) {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: url,
            method: "GET",
            // dataType: "json",
            // data: {
            //     playlist
            // },
            success: function (data) {
                // 비동기 작업 성공 시 호출
                resolve(data);
            },
            error: function (e) {
                // 비동기 작업 실패 시 호출
                reject(e);
            },
        });
    });
};

async function requestCurrentPlay(url) {
    try {
        console.log("재생목록 받아오기 ajax 실행 url : " + url);
        const result = await getCurrentPlayAjax(url);
        $("#currentPlayList_ul").empty();
        result.forEach(function(e){
            // let audioObject = new Audio();
            // audioObject.src=e.links[0].href;
            // console.log("ajaxtime : "+audioObject.duration);
            $("#currentPlayList_ul").append('<li><div class=\"playlist-dropUp-content-inner\"><div class=\"dropUp-inner-info\"><img src=\"' + e.links[1].href
                + '"/> <label class="playlist-dropUp-content-playBtn position-absolute"> <input type="button" id = \"' + e.links[0].href + '\" onclick = \"playAudio(this)\"><span class="material-icons position-absolute">play_circle</span></label><div class="dropUp-inner-info-text"><span>'+ e.title +'</span></div></div><div class="dropUp-inner-time"><button id="song-addInfo"><span class="material-icons">dehaze</span></button><div id="songInfo-dropbox"><button >삭제</button><button >플레이리스트 추가</button></div></div></div><div></div></li>');
        });
    } catch (error) {
        console.log("error : ", error);
    }
}

//     //이 페이지에서 뒤로가기 하거나, 목록을 누를때 그리고 새로고침을 할 때 이벤트 발생
// $(window).on("beforeunload", function (event) {
//     event.preventDefault();
//     //requestCurrentPlay("retrieveMusicList/" + $("#userNoForPlaylist").text() + "pl1");
//     // ajax
//
//     // 현재 시간이랑
//     //$("#audio").currentTime;
//     // 현재 음악의... 정보? 를 ajax로 넘겨서 security principal에 저장하고
//     //$("#audio").attr("src");
//
//     // 이후 requestCurrentPlay 에서 audio 셋팅해줘야할듯
// });
