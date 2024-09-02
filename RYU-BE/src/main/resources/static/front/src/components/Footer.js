import '../assets/Footer.scss';
const imageUrl = 'https://img.shields.io/badge/Github_Blog-%2396ABFB?style=for-the-badge&logo=github&logoColor=white';

const Footer = () => {
    return (
        <div className="Footer">
            <br/>
            <a href="https://mul-ryu.github.io/">
            <img src={imageUrl} />
            </a>
            <p>
            @ Powered By AtwoZ. All rights reserved.
            </p>
        </div>
    )
}

export default Footer;