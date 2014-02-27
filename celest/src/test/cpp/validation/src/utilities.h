
#pragma once

#ifndef CELEST_UTILITIES_H
#define CELEST_UTILITIES_H

/************************************************************************/
/* Windows specific                                                     */
/************************************************************************/

#ifdef _MSC_VER
#include <windows.h>
#include <iostream>

// See http://stackoverflow.com/questions/8233842/how-to-check-if-directory-exist-using-c-and-winapi
inline bool dirExists(const std::string& dir) {
    DWORD ftyp = GetFileAttributesA(dir.c_str());
    if (ftyp == INVALID_FILE_ATTRIBUTES) return false;  //something is wrong with your path!
    if (ftyp & FILE_ATTRIBUTE_DIRECTORY) return true;   // this is a directory!
    return false;    // this is not a directory!
}
#endif

/************************************************************************/
/* Utilities                                                            */
/************************************************************************/

#include <string>

inline std::string base_directory() {
#ifdef _MSC_VER
    if ( !dirExists("src/test/resources") ) {
        std::cerr << "Error: Start this program from the celest root folder so the path 'src/test/resources' exists! " << std::endl;
        throw std::runtime_error("The path src/test/resources relative to the current directory does not exist!");
    }
#endif
    return "src/test/resources/";
}

void printMatrix(double m[3][3]) {
    for (int row=0; row<3; ++row) {
        std::cout << "[\t";
        for (int col=0; col<3; ++col)
            std::cout << m[row][col] << "\t";
        std::cout << "]" << std::endl;
    }
}

#endif
