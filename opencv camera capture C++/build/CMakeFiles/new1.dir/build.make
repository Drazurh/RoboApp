# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 2.8

#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:

# Remove some rules from gmake that .SUFFIXES does not remove.
SUFFIXES =

.SUFFIXES: .hpux_make_needs_suffix_list

# Suppress display of executed commands.
$(VERBOSE).SILENT:

# A target that is always out of date.
cmake_force:
.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /usr/bin/cmake

# The command to remove a file.
RM = /usr/bin/cmake -E remove -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /home/alex/Desktop/opencvtest

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /home/alex/Desktop/opencvtest/build

# Include any dependencies generated for this target.
include CMakeFiles/new1.dir/depend.make

# Include the progress variables for this target.
include CMakeFiles/new1.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/new1.dir/flags.make

CMakeFiles/new1.dir/main.cpp.o: CMakeFiles/new1.dir/flags.make
CMakeFiles/new1.dir/main.cpp.o: ../main.cpp
	$(CMAKE_COMMAND) -E cmake_progress_report /home/alex/Desktop/opencvtest/build/CMakeFiles $(CMAKE_PROGRESS_1)
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Building CXX object CMakeFiles/new1.dir/main.cpp.o"
	/usr/bin/c++   $(CXX_DEFINES) $(CXX_FLAGS) -o CMakeFiles/new1.dir/main.cpp.o -c /home/alex/Desktop/opencvtest/main.cpp

CMakeFiles/new1.dir/main.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/new1.dir/main.cpp.i"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_FLAGS) -E /home/alex/Desktop/opencvtest/main.cpp > CMakeFiles/new1.dir/main.cpp.i

CMakeFiles/new1.dir/main.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/new1.dir/main.cpp.s"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_FLAGS) -S /home/alex/Desktop/opencvtest/main.cpp -o CMakeFiles/new1.dir/main.cpp.s

CMakeFiles/new1.dir/main.cpp.o.requires:
.PHONY : CMakeFiles/new1.dir/main.cpp.o.requires

CMakeFiles/new1.dir/main.cpp.o.provides: CMakeFiles/new1.dir/main.cpp.o.requires
	$(MAKE) -f CMakeFiles/new1.dir/build.make CMakeFiles/new1.dir/main.cpp.o.provides.build
.PHONY : CMakeFiles/new1.dir/main.cpp.o.provides

CMakeFiles/new1.dir/main.cpp.o.provides.build: CMakeFiles/new1.dir/main.cpp.o

# Object files for target new1
new1_OBJECTS = \
"CMakeFiles/new1.dir/main.cpp.o"

# External object files for target new1
new1_EXTERNAL_OBJECTS =

new1: CMakeFiles/new1.dir/main.cpp.o
new1: CMakeFiles/new1.dir/build.make
new1: /usr/local/lib/libopencv_videostab.so.3.1.0
new1: /usr/local/lib/libopencv_videoio.so.3.1.0
new1: /usr/local/lib/libopencv_video.so.3.1.0
new1: /usr/local/lib/libopencv_superres.so.3.1.0
new1: /usr/local/lib/libopencv_stitching.so.3.1.0
new1: /usr/local/lib/libopencv_shape.so.3.1.0
new1: /usr/local/lib/libopencv_photo.so.3.1.0
new1: /usr/local/lib/libopencv_objdetect.so.3.1.0
new1: /usr/local/lib/libopencv_ml.so.3.1.0
new1: /usr/local/lib/libopencv_imgproc.so.3.1.0
new1: /usr/local/lib/libopencv_imgcodecs.so.3.1.0
new1: /usr/local/lib/libopencv_highgui.so.3.1.0
new1: /usr/local/lib/libopencv_flann.so.3.1.0
new1: /usr/local/lib/libopencv_features2d.so.3.1.0
new1: /usr/local/lib/libopencv_core.so.3.1.0
new1: /usr/local/lib/libopencv_calib3d.so.3.1.0
new1: /usr/local/lib/libopencv_features2d.so.3.1.0
new1: /usr/local/lib/libopencv_ml.so.3.1.0
new1: /usr/local/lib/libopencv_highgui.so.3.1.0
new1: /usr/local/lib/libopencv_videoio.so.3.1.0
new1: /usr/local/lib/libopencv_imgcodecs.so.3.1.0
new1: /usr/local/lib/libopencv_flann.so.3.1.0
new1: /usr/local/lib/libopencv_video.so.3.1.0
new1: /usr/local/lib/libopencv_imgproc.so.3.1.0
new1: /usr/local/lib/libopencv_core.so.3.1.0
new1: CMakeFiles/new1.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --red --bold "Linking CXX executable new1"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/new1.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/new1.dir/build: new1
.PHONY : CMakeFiles/new1.dir/build

CMakeFiles/new1.dir/requires: CMakeFiles/new1.dir/main.cpp.o.requires
.PHONY : CMakeFiles/new1.dir/requires

CMakeFiles/new1.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/new1.dir/cmake_clean.cmake
.PHONY : CMakeFiles/new1.dir/clean

CMakeFiles/new1.dir/depend:
	cd /home/alex/Desktop/opencvtest/build && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /home/alex/Desktop/opencvtest /home/alex/Desktop/opencvtest /home/alex/Desktop/opencvtest/build /home/alex/Desktop/opencvtest/build /home/alex/Desktop/opencvtest/build/CMakeFiles/new1.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles/new1.dir/depend

