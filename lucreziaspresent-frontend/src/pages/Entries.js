import Container from "react-bootstrap/Container";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";
import { useEffect, useRef, useState } from "react";
import { readEntries } from "../utils/apiService";
import HTMLFlipBook from "react-pageflip";
import React from "react";
const Entries = () => {
  const [entries, setEntries] = useState([]);
  const book = useRef();
  console.log(book.current.pageFlip().getPageCount());

  useEffect(() => {
    readEntries(setEntries);
  }, []);
  return (
    <HTMLFlipBook width={300} height={500} ref={book}>
      <Page number="1">Page text</Page>
      <Page number="2">Page text</Page>
      <Page number="3">Page text</Page>
      <Page number="4">Page text</Page>
    </HTMLFlipBook>
  );
};

const Page = React.forwardRef((props, ref) => {
  return (
    <div className="demoPage" ref={ref}>
      <h1>Page Header</h1>
      <p>{props.children}</p>
      <p>Page number: {props.number}</p>
    </div>
  );
});
export default Entries;
