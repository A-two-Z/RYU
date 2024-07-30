import { Link, useLocation } from "react-router-dom";
import "../assets/Nav.scss"

export default function NavBar() {
    let location = useLocation();
    return (
        <nav className="nav">
            <ul className="nav-bar">
                <li>
                    <Link to="/product/">Product</Link>
                </li>
                <li>
                    <Link to="/order/">Order</Link>
                </li>
            </ul>
        </nav>
    );
}