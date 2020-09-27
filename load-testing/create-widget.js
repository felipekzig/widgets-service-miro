import http from 'k6/http';

export default function () {
    var url = 'http://localhost:8080/widgets';
    var payload = JSON.stringify({
        width: 200,
        height: 100,
        coords: {
            x: 10,
            y: 10
        },
        zIndex: 4
    });

    var params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    http.post(url, payload, params);
}