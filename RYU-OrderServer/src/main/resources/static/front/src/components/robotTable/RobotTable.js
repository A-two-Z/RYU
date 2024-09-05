import React, { useState, useEffect } from 'react';
import '../../assets/OrderList.scss';

function RobotTable() {
  const [robots, setRobots] = useState([]);
  const [selectedRobots, setSelectedRobots] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [newRobotName, setNewRobotName] = useState('');

  useEffect(() => {
    // Fetch robot data from the backend API
    fetch(process.env.REACT_APP_AWS_URI_ROBOT+'/api/robots')
      .then(response => response.json())
      .then(data => setRobots(data))
      .catch(error => console.error('Error fetching robot data:', error));
  }, []);  

  const handleCheckboxChange = (robotId) => {
    setSelectedRobots(prevSelectedRobots =>
      prevSelectedRobots.includes(robotId)
        ? prevSelectedRobots.filter(id => id !== robotId)
        : [...prevSelectedRobots, robotId]
    );
  };

  const handleRegisterClick = () => {
    setShowForm(true);
  };

  const handleFormSubmit = (e) => {
    e.preventDefault();
    // Send new robot data to the backend API using POST
    fetch(process.env.REACT_APP_AWS_URI_ROBOT+'/api/robots', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ name: newRobotName })
    })
      .then(response => response.json())
      .then(data => {
        console.log('Successfully registered robot:', data);
        setRobots(prevRobots => [...prevRobots, data]); // Update the robot list
        setShowForm(false); // Hide form after submission
        setNewRobotName(''); // Clear form input
      })
      .catch(error => console.error('Error registering robot:', error));
  };

  const handleDeleteClick = () => {
    // Send selected robots data to the backend API for deletion using DELETE
    fetch(process.env.REACT_APP_AWS_URI_ROBOT+'/api/robots', {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ ids: selectedRobots })
    })
      .then(response => response.text())
      .then(() => {
        setRobots(prevRobots => prevRobots.filter(robot => !selectedRobots.includes(robot.robotId)));
        setSelectedRobots([]); // Clear selected robots
      })
      .catch(error => console.error('Error deleting robots:', error));
  };

  return (
    <div className="orderlist">
      <h1>Robot List <sub>(administrator)</sub></h1>
      <table>
        <thead>
          <tr>
            <th>선택</th>
            <th>로봇 ID</th>
            <th>로봇명</th>
            <th>상태</th>
          </tr>
        </thead>
        <tbody>
          {robots.map(robot => (
            <tr key={robot.robotId}>
              <td>
                <input
                  type="checkbox"
                  checked={selectedRobots.includes(robot.robotId)}
                  onChange={() => handleCheckboxChange(robot.robotId)}
                />
              </td>
              <td>{robot.robotId}</td>
              <td>{robot.robotName}</td>
              <td>{robot.robotStatus}</td>
            </tr>
          ))}
        </tbody>
      </table>
      <button onClick={handleRegisterClick}>신규로봇 등록</button> &nbsp;
      <button onClick={handleDeleteClick} disabled={selectedRobots.length === 0}>로봇 삭제</button>
      
      {showForm && (
        <form onSubmit={handleFormSubmit}>
          <input
            type="text"
            value={newRobotName}
            onChange={(e) => setNewRobotName(e.target.value)}
            placeholder="로봇 이름 입력"
            required
          />
          <button type="submit">제출</button>
        </form>
      )}
    </div>
  );
}

export default RobotTable;
