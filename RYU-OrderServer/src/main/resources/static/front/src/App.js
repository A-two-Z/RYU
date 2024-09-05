import React from 'react';
import { Routes, Route } from 'react-router-dom';
import Page from './components/Page';
import Footer from './components/Footer';
import './assets/App.scss'
function App() {
  return (
      <div className="App">
        <Page />
        <Footer />
      </div>
  );
}

export default App;
