/**
 * @license
 * Copyright 2020 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import React from "react";
import { PureComponent, PureComponentProps, PureComponentState } from "../../../../components";
import { ErrorTabContent } from "./errorTab";
import { Services } from "src/services";
import MarkdownPreview from '@uiw/react-markdown-preview';



/**
 * Properties
 */
// tslint:disable-next-line:no-empty-interface
export interface MarkdownTabContentProps extends PureComponentProps {
    markdownContent: string;
}

/**
 * State
 */
// tslint:disable-next-line:no-empty-interface
export interface MarkdownTabContentState extends PureComponentState {
    markdownParsedContent: any | undefined;
    error: any | undefined
}


/**
 * Models the markdown content of the Artifact Markdown tab.
 */
export class MarkdownTabContent extends PureComponent<MarkdownTabContentProps, MarkdownTabContentState> {

    constructor(props: Readonly<MarkdownTabContentProps>) {
        super(props);
    }

    public render(): React.ReactElement {
        if (this.isError()) {
            return <ErrorTabContent error={{ errorMessage: "An error occurred while rendering the Markdown content", error: this.state.error }} />
        }

        let visualizer: React.ReactElement | null = null;

        // rehypeRewrite is required to disable links in headers
        visualizer = <div>
            <MarkdownPreview source={this.state.markdownParsedContent} style={{ padding: 22 }} rehypeRewrite={(node, index, parent) => {
                if (node.tagName === "a" && parent && /^h(1|2|3|4|5|6)/.test(parent.tagName)) {
                    parent.children = parent.children.slice(1)
                }
            }}></MarkdownPreview>
        </div>


        if (visualizer !== null) {
            return visualizer;
        } else {
            return <h1>Unsupported Markdown Type</h1>
        }
    }

    protected initializeState(): MarkdownTabContentState {
        try {
            return {
                markdownParsedContent: this.props.markdownContent,
                error: undefined
            };
        } catch (ex) {
            Services.getLoggerService().warn("Failed to parse content:");
            Services.getLoggerService().error(ex);
            return {
                markdownParsedContent: undefined,
                error: ex
            };
        }

    }

    private isError(): boolean {
        if (this.state.error) {
            return true;
        } else {
            return false;
        }
    }
}
