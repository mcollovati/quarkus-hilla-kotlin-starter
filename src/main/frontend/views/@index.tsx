import {Button, TextField} from "@vaadin/react-components";
import {HelloEndpoint} from "Frontend/generated/endpoints.js";
import {useState} from "react";
import type {ViewConfig} from "@vaadin/hilla-file-router/types.js";

export const config: ViewConfig = {
    menu: {
        title: "Main page"
    }
};

export default function MainView() {
    const [name, setName] = useState("");
    const [responses, setResponses] = useState<string[]>([]);

    return (
        <>
            <TextField
                label="Your name"
                onValueChanged={(e) => {
                    setName(e.detail.value);
                }}
            />
            <Button
                onClick={async () => {
                    const serverResponse = await HelloEndpoint.sayHello(name);
                    setResponses([serverResponse, ...responses]);
                }}
            >
                Say hello
            </Button>
            <ul>
                {responses.map((text, idx) => (<li className="message" key={idx}>{text}</li>))}
            </ul>
        </>
    );
}