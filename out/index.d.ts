/// <reference types="react" />
import * as React from "react";
import { ViewProperties, NativeSyntheticEvent } from 'react-native';
export interface UnityViewMessageEventData {
    message: string;
}
export interface UnityViewProps extends ViewProperties {
    /**
     * Receive message from unity.
     */
    onMessage?: (event: NativeSyntheticEvent<UnityViewMessageEventData>) => void;
}
export default class UnityView extends React.Component<UnityViewProps> {
    static propTypes: any;
    /**
     * Send Message to Unity.
     * @param gameObject The Name of GameObject. Also can be a path string.
     * @param methodName Method name in GameObject instance.
     * @param message The message will post.
     */
    postMessage(gameObject: string, methodName: string, message: string): void;
    /**
     * Send Message to UnityMessageManager.
     * @param message The message will post.
     */
    postMessageToUnityManager(message: string): void;
    private getViewHandle();
    private onMessage(event);
    render(): JSX.Element;
}
