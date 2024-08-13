// LoginPage.js
import React, { useState } from 'react';
import axios from 'axios';

function LoginPage() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');


    function handleLogin(){

        fetch(process.env.REACT_APP_AWS_URI+'/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            credentials: 'include', // 쿠키와 인증 정보를 포함한 요청
            body: JSON.stringify({
                'username': 'admin',
                'password':'admin' })
        })
            .then(response => {
                if (!response.ok) {
                    alert(response);
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }else{
                    alert("로그인에 성공했습니다.")
                    // const { token } = response.data;
                    alert(response.data);
                    console.log(response.ok);
                    localStorage.setItem('jwtToken', response.data); // JWT 토큰을 로컬 스토리지에 저장
                    window.location.href = '/order'; // 주문 페이지로 리디렉션
                }
            })
            .catch(error => {
                console.error('Error:', error.message);
                alert('로그인에 실패했습니다.');

                setTimeout(() => {
                    console.log('1 minute has passed since error.');
                    // 추가 작업 처리
                }, 60000); // 60000 milliseconds = 1 minute

            });
    }
    return (
        <div>
            <h1>로그인</h1>
            <form onSubmit={handleLogin}>
                <input
                    type="text"
                    placeholder="사용자명"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                />
                <input
                    type="password"
                    placeholder="비밀번호"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
                <button type="submit">로그인</button>
            </form>
        </div>
    );
}

export default LoginPage;
