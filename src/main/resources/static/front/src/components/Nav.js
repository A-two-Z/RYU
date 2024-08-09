import { NavLink, Link, useLocation } from "react-router-dom";
import "../assets/Nav.scss";
import logo from "../logo.svg";

export default function NavBar() {
  let location = useLocation();
  return (
    <nav className="nav">
      <h1>
        <img src={logo} />
        &nbsp;<span>Mul?Ryu</span>
      </h1>
      <ul className="nav-menu">
        <li>
          <NavLink to="/product" activeClassName="active">
            Product
          </NavLink>
        </li>
        <li>
          <NavLink to="/order" activeClassName="active">
            Order
          </NavLink>
        </li>
      </ul>
    </nav>
  );
}
