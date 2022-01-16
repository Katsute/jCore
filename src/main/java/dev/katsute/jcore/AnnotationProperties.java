/*
 * Copyright (C) 2021-2022 Katsute <https://github.com/Katsute>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package dev.katsute.jcore;

/**
 * Optional properties that can be sent with annotation commands (notice, error, and warning)
 *
 * @see AnnotationProperties.Builder
 * @author Katsute
 * @since 1.1.0
 * @version 1.3.0
 */
public class AnnotationProperties {

    String title, file;

    Integer startColumn,
            endColumn,
            startLine,
            endLine;

    /**
     * Creates annotation properties.
     *
     * @param title the title for the annotation
     * @param file file path
     * @param startColumn the start column for the annotation; startLine and endLine must match
     * @param endColumn the end column for the annotation; startLine and endLine must match
     * @param startLine the start line for the annotation
     * @param endLine the end line for the annotation
     */
    public AnnotationProperties(final String title, final String file, final Integer startColumn, final Integer endColumn, final Integer startLine, final Integer endLine){
        this.title = title;
        this.file = file;
        this.startColumn = startColumn;
        this.endColumn = endColumn;
        this.startLine = startLine;
        this.endLine = endLine;
    }

    @Override
    public String toString(){
        return "AnnotationProperties{" +
               "title='" + title + '\'' +
               ", file='" + file + '\'' +
               ", startColumn=" + startColumn +
               ", endColumn=" + endColumn +
               ", startLine=" + startLine +
               ", endLine=" + endLine +
               '}';
    }

    /**
     * Builder class for {@link AnnotationProperties}.
     *
     * @see AnnotationProperties
     * @author Katsute
     * @since 1.1.0
     * @version 1.1.0
     */
    public static class Builder {

        private String  title,
                        file;
        private Integer startColumn,
                        endColumn,
                        startLine,
                        endLine;

        /**
         * Creates a builder object for {@link AnnotationProperties.Builder}.
         *
         * @since 1.1.0
         */
        public Builder(){ }


        /**
         * The title for the annotation.
         *
         * @param title the title for the annotation
         * @return builder
         *
         * @since 1.1.0
         */
        public final Builder title(final String title){
            this.title = title;
            return this;
        }

        /**
         * The file path.
         *
         * @param file file
         * @return builder
         *
         * @since 1.3.0
         */
        public final Builder file(final String file){
            this.file = file;
            return this;
        }

        /**
         * The start column for the annotation; startLine and endLine must match.
         *
         * @param startColumn the start column for the annotation; startLine and endLine must match
         *
         * @return builder
         * @since 1.1.0
         */
        public final Builder startColumn(final Integer startColumn){
            this.startColumn = startColumn;
            return this;
        }

        /**
         * The end column for the annotation; startLine and endLine must match.
         *
         * @param endColumn the end column for the annotation; startLine and endLine must match
         * @return builder
         *
         * @since 1.1.0
         */
        public final Builder endColumn(final Integer endColumn){
            this.endColumn = endColumn;
            return this;
        }

        /**
         * The start line for the annotation.
         *
         * @param startLine the start line for the annotation
         * @return builder
         *
         * @since 1.1.0
         */
        public final Builder startLine(final Integer startLine){
            this.startLine = startLine;
            return this;
        }

        /**
         * The end line for the annotation.
         *
         * @param endLine the end line for the annotation
         * @return builder
         *
         * @since 1.1.0
         */
        public final Builder endLine(final Integer endLine){
            this.endLine = endLine;
            return this;
        }

        /**
         * Returns {@link AnnotationProperties}.
         *
         * @return {@link AnnotationProperties}
         *
         * @since 1.1.0
         */
        public final AnnotationProperties build(){
            return new AnnotationProperties(title, file, startColumn, endColumn, startLine, endLine);
        }

        @Override
        public String toString(){
            return "Builder{" +
                   "title='" + title + '\'' +
                   ", file='" + file + '\'' +
                   ", startColumn=" + startColumn +
                   ", endColumn=" + endColumn +
                   ", startLine=" + startLine +
                   ", endLine=" + endLine +
                   '}';
        }

    }

}
