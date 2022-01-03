$(document).ready(function () {
    document.getElementById("playlist-prev").addEventListener("click", playlistPrev);
    document.getElementById("playlist-pause").addEventListener("click", playlistPause);
    document.getElementById("playlist-play").addEventListener("click", playlistPlay);
    document.getElementById("playlist-next").addEventListener("click", playlistNext);
    document.getElementById("volume-mute").addEventListener("click", volumeMute);
    document.getElementById("playlist-heartEmpty").addEventListener("click", playlistHeartEmpty);
    document.getElementById("playlist-heart").addEventListener("click", playlistHeart);
    document.getElementById("playlist-psersonAdd").addEventListener("click", playlistPersonAdd);
    document.getElementById("playlist-songList").addEventListener("click", songList);

    function playlistPrev(){
        alert("이전 노래 재생 버튼");
    }


    function playlistPause() {
        alert("노래 정지 버튼");
    }

    function playlistPlay() {
        alert("노래 재생 버튼");
    }

    function playlistNext() {
        alert("다음 노래 재생 버튼");
    }

    function volumeMute() {
        alert("무소음 버튼");
    }

    function playlistHeartEmpty() {
        alert("좋아요 해제상태");
    }

    function playlistHeart() {
        alert("종아요 상태");
    }

    function playlistPersonAdd() {
        alert("노래 올린 사람 구독");
    }

    function songList() {
        alert("노래 리스트 보기 버튼");
    }
});
