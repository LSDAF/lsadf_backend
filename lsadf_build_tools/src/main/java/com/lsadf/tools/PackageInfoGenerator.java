/*
 * Copyright © 2024-2025 LSDAF
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lsadf.tools;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PackageInfoGenerator {

  private static final String LICENSE_HEADER =
      """
      /*
       * Copyright © 2024-2025 LSDAF
       *
       * Licensed under the Apache License, Version 2.0 (the "License");
       * you may not use this file except in compliance with the License.
       * You may obtain a copy of the License at
       *
       *     http://www.apache.org/licenses/LICENSE-2.0
       *
       * Unless required by applicable law or agreed to in writing, software
       * distributed under the License is distributed on an "AS IS" BASIS,
       * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
       * See the License for the specific language governing permissions and
       * limitations under the License.
       */
      """;

  private static final String PACKAGE_INFO_JAVA = "package-info.java";
  private static final String JAVA_EXTENSION = ".java";

  public static void main(String[] args) throws IOException {
    Path srcDir = Paths.get(args[0]);
    Path destDir = Paths.get(args[1]);
    try (Stream<Path> paths = Files.walk(srcDir)) {
      paths
          .filter(Files::isDirectory)
          .forEach(
              dir -> {
                try (var list = Files.list(dir)) {
                  boolean hasJavaFiles =
                      list.anyMatch(
                          p ->
                              p.toString().endsWith(JAVA_EXTENSION)
                                  && !p.getFileName().toString().equals(PACKAGE_INFO_JAVA));

                  if (hasJavaFiles) {
                    Path pkgInfo = dir.resolve(PACKAGE_INFO_JAVA);
                    if (!Files.exists(pkgInfo)) {
                      // Compute package name relative to srcDir
                      String pkgName =
                          srcDir.relativize(dir).toString().replace(File.separatorChar, '.');

                      Path targetDir = destDir.resolve(srcDir.relativize(dir));
                      Files.createDirectories(targetDir);

                      Path targetFile = targetDir.resolve(PACKAGE_INFO_JAVA);
                      String content =
                          LICENSE_HEADER
                              + """
                                    @org.jspecify.annotations.NullMarked
                                    package %s;
                                    """
                                  .formatted(pkgName);

                      Files.writeString(targetFile, content);
                      log.info("Generated package-info.java for {}", pkgName);
                    }
                  }
                } catch (IOException e) {
                  throw new UncheckedIOException(e);
                }
              });
    }
  }
}
