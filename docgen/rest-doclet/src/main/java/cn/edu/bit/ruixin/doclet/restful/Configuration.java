/*******************************************************************************
 * Copyright (C) 2014 The Calrissian Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package cn.edu.bit.ruixin.doclet.restful;

import cn.edu.bit.ruixin.doclet.restful.writer.simple.SimpleHtmlWriter;

import static java.lang.Boolean.parseBoolean;

public class Configuration {

    private enum ConfigOption {
        OUTPUT_FORMAT("o", SimpleHtmlWriter.OUTPUT_OPTION_NAME),

        //Legacy Options
        TITLE("t", "睿信社区技术保障中心<br>前后端交互文档"),
        STYLESHEET("stylesheet", "./stylesheet.css"),

        //Swagger options
        API_VERSION("version", null),
        DISPLAY_ONLY("callable", "true"),
        BASEPATH("path", "/");

        private String option;
        private String defaultValue;

        private ConfigOption(String option, String defaultValue) {
            this.option = "-" + option;
            this.defaultValue = defaultValue;
        }

        public String getOption() {
            return option;
        }

        public String getDefaultValue() {
            return defaultValue;
        }
    }

    private String[][] options;

    public Configuration(String[][] options) {
        this.options = options;
    }

    public String getOutputFormat() {
        return getOption(ConfigOption.OUTPUT_FORMAT);
    }

    public String getDocumentTitle() {
        return getOption(ConfigOption.TITLE);
    }

    public String getStyleSheet() {
        return getOption(ConfigOption.STYLESHEET);
    }

    public String getApiVersion() {
        return getOption(ConfigOption.API_VERSION);
    }

    public String getPath() {
        return getOption(ConfigOption.BASEPATH);
    }

    public boolean isCallable() {
        return parseBoolean(getOption(ConfigOption.DISPLAY_ONLY));
    }

    public boolean isdefaultStyleSheet() {
        return getOption(ConfigOption.STYLESHEET.getOption(), null) == null;
    }

    private String getOption(ConfigOption configOption) {
        return getOption(configOption.getOption(), configOption.getDefaultValue());
    }

    private String getOption(String name, String defaultValue) {
        for (String[] option : options) {
            if (option[0].equals(name)) {
                return option[1];
            }
        }
        return defaultValue;
    }

    public static int getOptionLength(String option) {

        for (ConfigOption configOption : ConfigOption.values())
            if (option.equals(configOption.getOption()))
                return 2;

        return 0;
    }
}
