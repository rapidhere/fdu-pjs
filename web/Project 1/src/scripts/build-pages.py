#-*- coding: utf8 -*-
"""
Author: rapidhere@gmail.com

Build jade templates to web pages

Output directory is pages

This framework is just a workaround for project 1 without backend
"""
import os
import subprocess
import simplejson

# get working dir
WORKING_DIR = os.path.realpath(os.path.join(os.path.dirname(__file__), ".."))

# get other dirs
OUTPUT_ROOT = os.path.join(WORKING_DIR, "pages")
TEMPLATE_ROOT = os.path.join(WORKING_DIR, "template")
JADE_ROOT = os.path.join(TEMPLATE_ROOT, "jade")


# build recursively
def _build(jade_dir, output_dir):
    print "building directory: " + jade_dir

    for fname in os.listdir(jade_dir):
        full_path = os.path.join(jade_dir, fname)
        full_output_path = os.path.join(output_dir, fname)

        if os.path.isdir(full_path):
            # create directory if need
            if os.path.isdir(full_output_path):
                os.mkdir(full_output_path)
                _build(full_path, full_output_path)
        elif fname.endswith(".jade"):
            print "building " + full_path

            full_output_path = full_output_path[:-5]  + ".html"

            if os.path.isfile(full_output_path):
                os.remove(full_output_path)

            subprocess.check_call(["jade", full_path, "-o", os.path.dirname(full_output_path), "-P", "--no-debug",
                "-O", os.path.join(WORKING_DIR, "scripts", "options.json")])

# main
if __name__ == "__main__":
    print "Working directory is: " + WORKING_DIR

    # just a workaround
    simplejson.dump({
            "static": "../static",
            "hotUsers": [{
                "name": "Mark Morton tttt",
                "portrait": "../static/img/portrait-mark morton.jpg",
                "description": "Guitarist From Lamb of God"
            }, {
                "name": "Mark Morton",
                "portrait": "../static/img/portrait-mark morton.jpg",
                "description": "Guitarist From Lamb of God"
            }, {
                "name": "Mark Morton",
                "portrait": "../static/img/portrait-mark morton.jpg",
                "description": "Guitarist From Lamb of God"
            }, {
                "name": "Mark Morton",
                "portrait": "../static/img/portrait-mark morton.jpg",
                "description": "Guitarist From Lamb of God"
            }, {
                "name": "Mark Morton",
                "portrait": "../static/img/portrait-mark morton.jpg",
                "description": "Guitarist From Lamb of God"
            }, {
                "name": "Mark Morton",
                "portrait": "../static/img/portrait-mark morton.jpg",
                "description": "Guitarist From Lamb of God"
            }, {
                "name": "Mark Morton",
                "portrait": "../static/img/portrait-mark morton.jpg",
                "description": "Guitarist From Lamb of God"
            }, {
                "name": "Mark Morton",
                "portrait": "../static/img/portrait-mark morton.jpg",
                "description": "Guitarist From Lamb of God"
            }]
        },
        file(os.path.join(WORKING_DIR, "scripts", "options.json"), "w"))

    # build output dir
    if not os.path.isdir(OUTPUT_ROOT):
        os.mkdir(OUTPUT_ROOT)

    # start to build
    _build(JADE_ROOT, OUTPUT_ROOT)
